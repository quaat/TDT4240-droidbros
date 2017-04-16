var supertest   = require('supertest');
var should      = require('should');

/**
 * Socket tests must be run individually (server closing dont work)
 *
 * API TESTS
 * >> npm test --grep test/integration/api
 *
 * SOCKET TESTS
 * >> npm test --grep test/integration/socket
 * 
 * UNIT TESTS
 * >> npm test --grep test/unit
 */

describe('Do not test it all together, wont work (dont know why..)', function(){
  it('RUN API TESTS 			>> npm test --grep test/integration/api', function(done) {
    done();
  })

  it('RUN SOCKET TESTS EXAMPLE1 	>> npm test --grep test/integration/socket/socket-connect.test.js', function(done) {
    done();
  })

  it('RUN SOCKET TESTS EXAMPLE2 	>> npm test --grep test/integration/socket/socket-game.test.js', function(done) {
    done();
  })

  it('RUN UNIT TESTS 			>> npm test --grep test/unit', function(done) {
    done();
  })
});