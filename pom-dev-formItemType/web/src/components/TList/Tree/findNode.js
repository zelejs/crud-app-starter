/**
 * 根据 id 找到节点
 *
 * @param {string} id
 * @param {object} treeData
 * @returns object node
 */
function findNode(id, treeData) {
  const stack = [];
  if (Array.isArray(treeData)) {
    stack.push(...treeData);
  } else {
    stack.push(treeData);
  }
  let rst;
  while (stack.length) {
    const item = stack.shift();
    if (String(item.id) === id) {
      rst = item;
      break;
    }
    if (Array.isArray(item.children)) {
      stack.push(...item.children);
    }
  }

  return rst;
}

export default findNode;