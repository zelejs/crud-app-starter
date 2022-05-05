var path = require('path');
var fs = require('fs-extra');

var distPath = path.resolve(process.cwd(), './dist');

fs.moveSync(`${distPath}/navigation-ui/index.html`, `${distPath}/navigation-ui.html`);
// fs.moveSync(`${distPath}/static/x`, `${distPath}/x`);