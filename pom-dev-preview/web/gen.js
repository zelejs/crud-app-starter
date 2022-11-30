var path = require('path');
var fs = require('fs-extra');

var distPath = path.resolve(process.cwd(), './dist');
var publicPath = path.resolve(process.cwd(), './public');

fs.removeSync(`${distPath}/config.js`);
fs.copyFileSync(`${publicPath}/config.js`, `${distPath}/preview-ui/config.js`);
// fs.moveSync(`${distPath}/static/x`, `${distPath}/x`);