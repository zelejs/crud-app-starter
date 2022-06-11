var path = require('path');
var fs = require('fs-extra');

var distPath = path.resolve(process.cwd(), './dist');

fs.moveSync(`${distPath}/formItemType-ui/index.html`, `${distPath}/formItemType-ui.html`);
fs.moveSync(`${distPath}/cloud.png`, `${distPath}/formItemType-ui/cloud.png`);
fs.moveSync(`${distPath}/color.less`, `${distPath}/formItemType-ui/color.less`);
fs.moveSync(`${distPath}/config.js`, `${distPath}/formItemType-ui/config.js`);
fs.moveSync(`${distPath}/error_img.png`, `${distPath}/formItemType-ui/error_img.png`);