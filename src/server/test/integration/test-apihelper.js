module.exports.login = function (agent, data, callback) {
  agent
    .post('/api/authenticate')
    .send(data)
    .expect('Content-type', /json/)
    .expect(200)
    .end(callback);
}

module.exports.register = function(agent, data, callback) {
  agent
    .post('/api/register')
    .send(data)
    .expect('Content-type',/json/)
    .expect(200)
    .end(callback);
}

module.exports.user = function(agent, data, callback) {
  agent
    .get('/api/user')
    .send(data)
    .expect('Content-type', /json/)
    .end(callback);
}

module.exports.games = function(agent, data, callback) {
  agent
    .get('/api/user/games')
    .send(data)
    .expect('Content-type', /json/)
    .end(callback);
}

module.exports.update = function(agent, data, callback) {
  agent
    .post('/api/user')
    .send(data)
    .expect('Content-type', /json/)
    .end(callback);
}

module.exports.changePassword = function(agent, data, callback) {
  agent
    .post('/api/user/password')
    .send(data)
    .expect('Content-type', /json/)
    .end(callback);
}

module.exports.removeUser = function(agent, data, callback) {
    agent
      .post('/api/user/delete')
      .send(data)
      .expect('Content-type', /json/)
      .end(callback);
}

module.exports.logout = function(agent, data, callback) {
    agent
      .post('/logout')
      .expect('Content-type', /json/)
      .end(callback);
}