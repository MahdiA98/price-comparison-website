window.onload = getPaginationNumber(), loadPageGame(1); //Initiates pagination and first page of games

//Method that searches for a game, using input from client side
function searchGame() {

    let gameName = document.getElementById("searchText").value;
    let url = "/search?product_name='" + gameName + "'";
    let htmlStr;

    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (xhttp.readyState == 4 && xhttp.status == 200) {

            //If response returned as invalid, return error message to client
            if (JSON.parse(this.responseText) == "invalid") {
                document.getElementById("searchResult").innerHTML = "<h2>Could not find game.</h2>";
                document.getElementById("homePage").innerHTML = "";
                document.getElementById("containerPagination").innerHTML = "";
                document.getElementById("containerComparison").innerHTML = "";
                document.getElementById("containerComparison2").innerHTML = "";
            }
            else {
                let resultList = JSON.parse(this.responseText);
    
                //Create table with 4 games per row
                htmlStr = "<table>"; 
                for (i = 0; i < resultList.length; i++) {

                    if ((i % 4) == 0 || i == 0) {
                        htmlStr += "<tr>";
                    }
                    if (resultList[i].image == "") {
                        resultList[i].image = "images/placeholder.png"; //Website HTML changed so inserted placeholder 
                    }
                    //Onclick functions added to load appropriate data for selected game
                    htmlStr += "<td onclick=loadGameAndComparisons(" + i + ")><img id='homePageImage' src='"+ resultList[i].image
                        + "'>" + "<p id=searchTableData" + i + ">" + resultList[i].name + "</td>";
                    }
                htmlStr += "</table>";
    
                //Empties and sets HTML
                document.getElementById("homePage").innerHTML = htmlStr;
                document.getElementById("searchResult").innerHTML = "";
                document.getElementById("containerComparison").innerHTML = "";
                document.getElementById("containerComparison2").innerHTML = "";
                document.getElementById("containerPagination").innerHTML = "";
                document.getElementById("containerPagination").innerHTML = "";
            }
            
        }
    };
    xhttp.open("GET", url, true);
    xhttp.setRequestHeader("Content-type", "application/json");
    xhttp.send();
}

//Method that gets the chosen game and comparison
function loadGameAndComparisons(number) {

    //Extracts input from client side
    let gameName = document.getElementById("searchTableData" + number).innerHTML;

    //Sets URL for GET request
    let url = "/search/loadgame?product_name='" + gameName + "'";

    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (xhttp.readyState == 4 && xhttp.status == 200) {

            //Parsing in response
            let resultList = JSON.parse(this.responseText);

            //Creating HTML elements using response
            //First table includes game information
            let html = "<table><tr><td><img src='" + resultList[0].image + "'></td>" +
                            "<td id='comparisonGameTitle'>" + resultList[0].name +
                            "<p>Write a review</p><br><p>Platform - " + resultList[0].title + "</td></tr></table>";

            //Second table includes comparison information
            let htmlStr = "<table>";
            htmlStr += "<tr id='comparisonRow'><td>Website:</td><td>Price:</td><td>Link:</td>";
            for (i = 0; i < resultList.length; i++) {
                htmlStr += "<tr><td>" + resultList[i].website_name + "</td>"
                        + "<td>" + "£" + resultList[i].price + "</td><td><a id='link' href='"
                        + resultList[i].link + "'> [click to visit page] </td></tr>";
            }
            htmlStr += "</table>";
            
            //Empties and sets HTML 
            document.getElementById("previousPage").innerHTML = "[return to home page]";
            document.getElementById("containerComparison").innerHTML = html;
            document.getElementById("containerComparison2").innerHTML = htmlStr;
            document.getElementById("homePage").innerHTML = "";
            document.getElementById("containerGameTable").innerHTML = "";
            document.getElementById("containerPagination").innerHTML = "";
            document.getElementById("searchResults").innerHTML = "";
        }
    }
    //Setting up GET requests   
    xhttp.open("GET", url, true);
    xhttp.setRequestHeader("Content-type", "application/json");
    xhttp.send();
}

//Method that sets up pagination for web site
function getPaginationNumber() {

    //URL for GET request
    let url = "/load_count";
  
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (xhttp.readyState == 4 && xhttp.status == 200) {

            //Dividing total number of games by 20, to limit page with 20 products
            let numOfPages = (Math.ceil(this.responseText / 20));

            //Creating pagination 
            //Set with onclick functions with preset parameter values, based on their incremented value, i
            //E.g <a>4</a> when clicked, would invoke loadPageGame(4)
            let htmlStr = "<div class='pagination'><a id=pageNumber1 onclick=loadPageGame(1)>&laquo;</a>";
            for (i = 1; i <= numOfPages; i++) {
                htmlStr += ("<a id=pageNumber" + i + " onclick=loadPageGame(" + i + ")>" + i + "</a>");
            }
            htmlStr += "<a id=pageNumber" + numOfPages + " onclick=loadPageGame(" + numOfPages + ")>" 
                    + "&raquo;</a></div>";

            //Set HTML element
            document.getElementById("containerPagination").innerHTML = htmlStr;
        }
    }
    xhttp.open("GET", url, true);
    xhttp.setRequestHeader("Content-type", "application/json");
    xhttp.send();
}

//Method that retrieves 20 games and loads into page
function loadPageGame(pageNumber) {

    //Offset and URL defined
    let offset = ((pageNumber - 1) * 20);
    let url = "/games?num_items=20&offset=" + offset;
    
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (xhttp.readyState == 4 && xhttp.status == 200) {

            var resultList = JSON.parse(this.responseText);
            let priceDecimals = ".00";

            //Table created to display result
            let htmlStr = "<table>"; 
            for (i = 0; i < resultList.length; i++) {

                //New row created after the 4th product, so 4 games per row
                if ((i % 4) == 0 || i == 0) {
                    htmlStr += "<tr>";
                }

                //Appending priceDecimals to prices with whole values 45 -> 45.00
                if (resultList[i].price.toString().length == 2) {
                    resultList[i].price += priceDecimals;
                }
                
                //Website changed HTML last minute, so inserted placeholder
                if (resultList[i].image == "") {
                    resultList[i].image = "images/placeholder.png";
                }

                //Onclick functions set to load comparison and game information
                htmlStr += "<td onclick=loadGameAndComparisons(" + i + ")><img id='homePageImage' src='"+ resultList[i].image
                        + "'>" + "<p id=searchTableData" + i + ">" + resultList[i].name + "</p>"
                        + "<p id='homePagePrice'>" +"$" + resultList[i].price + "</p></td>";
            }
            htmlStr += "</table>";
    
            //Setting HTML element by Id
            document.getElementById("homePage").innerHTML = htmlStr;
            document.getElementById("previousPage").innerHTML = "";
            document.getElementById("searchResult").innerHTML = "";
            document.getElementById("containerComparison").innerHTML = "";
            document.getElementById("containerComparison2").innerHTML = "";
            document.getElementById("containerPagination").innerHTML = getPaginationNumber();

        }
    }
    xhttp.open("GET", url, true);
    xhttp.setRequestHeader("Content-type", "application/json");
    xhttp.send(); 
}



