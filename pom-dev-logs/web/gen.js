var path = require('path');
var fs = require('fs-extra');

var distPath = path.resolve(process.cwd(), './dist');

fs.moveSync(`${distPath}/logs-ui/index.html`, `${distPath}/logs-ui.html`);
// fs.moveSync(`${distPath}/static/x`, `${distPath}/x`);