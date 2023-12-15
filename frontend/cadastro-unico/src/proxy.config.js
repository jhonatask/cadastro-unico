const proxy = [
    {
      context: '/api',
      target: 'http://localhost:8080',
      pathRewrite: {'^/clientes' : ''}
    }
  ];
  module.exports = proxy;