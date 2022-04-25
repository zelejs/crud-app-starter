var path = require('path');
var fs = require('fs-extra');

var distPath = path.resolve(process.cwd(), './dist');

fs.moveSync(`${distPath}/dev-logs/index.html`, `${distPath}/dev-logs.html`);
// fs.moveSync(`${distPath}/static/x`, `${distPath}/x`);