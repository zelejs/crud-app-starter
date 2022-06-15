var path = require('path');
var fs = require('fs-extra');

var distPath = path.resolve(process.cwd(), './dist');

fs.moveSync(`${distPath}/valueType-ui/index.html`, `${distPath}/valueType-ui.html`);
fs.moveSync(`${distPath}/cloud.png`, `${distPath}/valueType-ui/cloud.png`);
fs.moveSync(`${distPath}/color.less`, `${distPath}/valueType-ui/color.less`);
fs.moveSync(`${distPath}/config.js`, `${distPath}/valueType-ui/config.js`);
fs.moveSync(`${distPath}/error_img.png`, `${distPath}/valueType-ui/error_img.png`);