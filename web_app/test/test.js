require('../server.js');

//Built in Node.js assertions
const assert = require ("assert");

//Chai library for HTTP requests and more sophisticated assertions
let chai = require('chai');
let should = chai.should();
let chaiHttp = require ('chai-http');
chai.use(chaiHttp); 

//Import server
let server = require('../server');

/*
    All tests run fine, but due to one of the scrapers breaking, it might cause errors in test 3
    This is due to the trailing whitespace at the end of the name which isn't accounted for in the test
*/

describe('Server', function() {

    //Testing RESTful service #1
    describe('/GET games', () => {
        it('should GET name, image and price of all games', (done) => {
            chai.request(server)
                .get('/games')
                .end((err, res) => {
                    
                    //Normal checks
                    res.should.have.status(200);
                    res.body.should.be.a('array');

                    //Should have following properties
                    if (res.body.length > 1) {
                        res.body[0].should.have.property('name');
                        res.body[0].should.have.property('image');
                        res.body[0].should.have.property('price');
                    }
                    done();
                });
        });
    });

    //Testing RESTful service #2
    describe('/GET search', () => {
        it('should GET name of similar games', (done) => {
            let name = 'Call of Duty'; //name partially entered to test sql "LIKE" query
            chai.request(server)
                .get("/search?product_name='" + name + "'")
                .end((err, res,) => {

                    //normal checks
                    res.should.have.status(200);
                    res.body.should.be.a('array');
                    res.body[0].should.have.property('name');

                    let outputName;

                    for (let i = 0; i < res.body.length; i++) {
                        if (res.body[i].name == "Call of Duty Vanguard " || res.body[i].name == "Call of Duty Vanguard") {
                            outputName = res.body[i].name.substring(0, res.body[i].name.length - 1); // text has whitespace at end of name, so removed it.
                        }
                    }
                    
                    // result should return a game with this name, inside the array
                    assert(outputName, "Call of Duty Vanguard");
                    
                    // For call of duty, there should be more than one similar named game
                    // i.e call of duty black ops, call of duty cold war etc
                    assert(res.body.length >= 1);
                    done();
                });
        });
    });

    //Testing RESTful service #3
    describe('/GET search/loadgame', () => {
        it('should GET 1 or more comparisons for a specific game', (done) => {
            let name = 'Call of Duty Vanguard';
            chai.request(server)
                .get("/search/loadgame?product_name='" + name + "'")
                .end((err, res,) => {

                    //Normal checks
                    res.should.have.status(200);
                    res.body.should.be.a('array');

                    //Result array should contain the following properties
                    res.body[0].should.have.property('name');
                    res.body[0].should.have.property('price');
                    res.body[0].should.have.property('image');
                    res.body[0].should.have.property('link');
                    res.body[0].should.have.property('website_name');
                    res.body[0].should.have.property('title');

                    //Body text had whitespace at end of name, so removed it.
                    let outputName = res.body[0].name.substring(0, res.body[0].name.length - 1);
                    assert.equal(outputName, "Call of Duty Vanguard");
                    
                    //Game should have minimum 1 comparison, and a maximum of 5
                    assert(res.body.length >= 1);
                    assert(res.body.length <= 5);

                    done();
                });
        });
    });

    //Testing RESTful service #4
    describe('/GET total_count', () => {
        it('should GET total number of games', (done) => {
            chai.request(server)
                .get('/load_count')
                .end((err, res) => {

                    //Normal checks
                    res.should.have.status(200);

                    //Database should have more than 100 games
                    assert(res.body > 100);
                    done();
                });
        });
    });

    //Testing RESTful service #5
    describe('/GET limit + offset', () => {
        it('should GET a total of 20 games, from the 50th entry (game.id) of the database', (done) => {
            chai.request(server)
                .get('/games?num_items=20&offset=49')
                .end((err, res) => {

                    //Normal checks
                    res.should.have.status(200);

                    //Length should be 20 and first id=50
                    assert.equal(res.body.length, 20);
                    assert.equal(res.body[0].id, 50);
                    done();
                });
        });
    });


});

