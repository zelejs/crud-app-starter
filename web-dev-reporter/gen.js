var path = require('path');
var fs = require('fs-extra');

var distPath = path.resolve(process.cwd(), './dist');

fs.moveSync(`${distPath}/model-house/index.html`, `${distPath}/model-house.html`);
// fs.moveSync(`${distPath}/static/x`, `${distPath}/x`);