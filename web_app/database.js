//Importing sql
const mysql = require("mysql");

//Setting up connection pool
var connectionPool = mysql.createPool({
    connectionLimit: 1,
    host: "localhost",
    user: "root",
    password: "",
    database: "price_comparison",
    debug: false
});

//Method that gets total number of games in database
exports.getTotalCount = (response) => {

    //query
    let sql = "SELECT COUNT(*) FROM games";

    connectionPool.query(sql, (err, result) => {
        //Checks for errors
        if (err) {
            let errorMsg = "{Error: " + err + "}";
            console.error(errorMsg);
            response.status(400).json(errMsg);
        }

        //sets variable to result count
        var totNumItems = result[0]['COUNT(*)'];

        //sends response with variable
        response.json(totNumItems);

    });
}

//Searches for games in the database using the input from the client side
exports.searchGames = (response, name) => {

    //Apostrophe was causing problems, so I removed them
    var str = name.replace(/'/g, '');

    //Query
    var sql = "SELECT name, image FROM games WHERE name LIKE '%" + str + "%' LIMIT 20";

    connectionPool.query(sql, function (err, result) {
        //Check for errors
        if (err){
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({'error': true, 'message': + err});
            return;
        }
        //If there is atleast 1 result, send response with result
        if (result[0] != undefined) {
            response.json(result);
        }
        //Else send error message
        else {
            response.send(JSON.stringify("invalid"));
        }
    });
}

//Returns comparison, platform and game data for selected product
function getComparison(response, id) {
 
    //Query
    var sql = "SELECT website_name, comparison.price, comparison.link, games.name, games.image, "
            + "platform.title FROM comparison "
            + "INNER JOIN games ON comparison.game_id = games.id INNER JOIN platform ON games.platform_id = platform.id "  
            + "WHERE comparison.game_id=" + id  + " ORDER BY comparison.price";

    connectionPool.query(sql, function (err, result) {
        //Check for errors
        if (err){
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({'error': true, 'message': + err});
            return;
        }

        //Send back response
        response.json(result);
    });
}

//Gets game from database and invokes callback function
exports.getGame = (response, name) => {
    
    //Query
    var sql = "SELECT id, name FROM games WHERE name=" + name;

    connectionPool.query(sql, function (err, result) {
        //Check for errors
        if (err){
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({'error': true, 'message': + err});
            return;
        }

        //If there is atleast one result, set variable with the first result id, pass it in and call getComparison
        if (result[0] != undefined) {
            var comparisonID = result[0]['id'];
            getComparison(response, comparisonID);
        }
        //Else return error message
        else {
            response.send(JSON.stringify("invalid"));
        }
    });
}

//Method that gets name, image and price, limited to 20 games and a specified offset
exports.getPageGames = (response, numItems, offset) => {

    //Query
    var sql = "SELECT games.id, games.name, games.image, comparison.price FROM comparison INNER JOIN "
            + "games ON comparison.game_id = games.id GROUP BY games.id ";

    //Add LIMIT and OFFSET if defined
    if(numItems !== undefined && offset !== undefined ){
        sql += "LIMIT " + numItems + " OFFSET " + offset;
    }

    connectionPool.query(sql, function (err, result) {
        //Check for errors
        if (err){
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({'error': true, 'message': + err});
            return;
        }
        //Send back response
        response.json(result);
    });
}

