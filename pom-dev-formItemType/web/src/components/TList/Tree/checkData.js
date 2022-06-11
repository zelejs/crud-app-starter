/**
 * 读取后台返回的扁平数据, 从数据中找到所需的数组数据
 * @param {array} rspData 后台返回的 response 的数据
 * @returns {array} listData 返回数组数据
 */
function checkData(rspData) {
  const listData = Array.isArray(rspData) ? rspData :
    Array.isArray(rspData.records) ? rspData.records : rspData.children;

  if (!Array.isArray(listData)) {
    throw new Error('后台未返回预期数据格式');
  }
  return listData;
}

export default checkData;