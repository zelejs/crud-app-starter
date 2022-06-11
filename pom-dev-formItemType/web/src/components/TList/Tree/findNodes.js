/**
 * 找到含有相同标题的 node
 *
 * @param {string} value 需要找的标题的值
 * @param {object} treeDat 树状数据结构
 * @returns {array} 找到的数据列表 
 */
function findNodes(value, treeData) {
  const stack = [treeData];
  const rst = [];
  while (stack.length) {
    const item = stack.shift();
    const { title = '', children } = item;
    if (title.indexOf(value) > -1) {
      rst.push(item);
    }
    if (Array.isArray(children)) {
      stack.push(...children);
    }
  }
  return rst;
}

export default findNodes;