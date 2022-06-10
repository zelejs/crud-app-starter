var path = require('path');
var fs = require('fs-extra');

var distPath = path.resolve(process.cwd(), './dist');

fs.moveSync(`${distPath}/connection-ui/index.html`, `${distPath}/connection-ui.html`);
// fs.moveSync(`${distPath}/static/x`, `${distPath}/x`);