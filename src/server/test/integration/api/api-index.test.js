var request     = require('supertest');
var should      = require('should');

describe('index', function(){
  var server;
  before(function(done) {
    server = require('../../../server');
    done();
  });
  after(function(done) {
    server.close();
    done();
  });

  it('Should respons to /', function(done) {
    request(server)
      .get('/')
      .expect(200, done);
  });

  it('Should error 404', function(done) {
    request(server)
      .get('/404')
      .expect(404, done);
  });
});