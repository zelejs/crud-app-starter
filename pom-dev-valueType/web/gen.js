var path = require('path');
var fs = require('fs-extra');

var distPath = path.resolve(process.cwd(), './dist');

fs.moveSync(`${distPath}/valueItemType-ui/index.html`, `${distPath}/valueItemType-ui.html`);
fs.moveSync(`${distPath}/cloud.png`, `${distPath}/valueItemType-ui/cloud.png`);
fs.moveSync(`${distPath}/color.less`, `${distPath}/valueItemType-ui/color.less`);
fs.moveSync(`${distPath}/config.js`, `${distPath}/valueItemType-ui/config.js`);
fs.moveSync(`${distPath}/error_img.png`, `${distPath}/valueItemType-ui/error_img.png`);