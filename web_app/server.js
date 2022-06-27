//Import the express and url modules
var express = require('express');
var url = require("url");

//Status codes defined in external file
require('./http_status');

var app = express();

//Import the mysql module
const path = require('path');

//Import database functions
const database = require("./database");

//Adds public folder for static web pages
app.use(express.static('public'));

//Starts app, listening on port 8080
app.listen(8080);

console.log("\nRunning program...\n");

//GET requests
app.get('/games', handleGetRequest);  
app.get('/search', handleGetRequest);
app.get('/search/loadgame', handleGetRequest);
app.get('/load_count', handleGetRequest);

//Function that handles the GET requests
function handleGetRequest(request, response){

    //Parse the URL
    var urlObj = url.parse(request.url, true);

    //Extract object containing queries from URL object.
    var queries = urlObj.query;

    //Variables to store queries values
    var numItems = queries['num_items'];
    var offset = queries['offset'];
    var search = queries['product_name'];

    //Split the path of the request into its components
    var pathArray = urlObj.pathname.split("/");
    var pathEnd = pathArray[pathArray.length - 1];

    //Several conditions that execute based on pathEnd
    //Executed using database functions imported 

    //Gets 20 games and loads into page
    if(pathEnd === "games"){
        database.getPageGames(response, numItems, offset);
        return;
    }

    //Searchs for games with similar name
    if (pathEnd == "search") {
        database.searchGames(response, search);
        return;
    }

    //Loads game and comparison for selected product
    if (pathEnd == "loadgame") {
        database.getGame(response, search);
        return;
    }

    //Returns total number of games 
    if (pathEnd == "load_count") {
        database.getTotalCount(response); 
        return;
    }

    response.status(HTTP_STATUS.NOT_FOUND);
    response.send("{error: 'Path not recognized', url: " + request.url + "}");
}


//Exporting app for testing
module.exports = app;
