    
/**
 * 
 * @param {object} data 数据源
 * @param {object} filter 过滤条件
 */
module.exports = function filterData(data={}, filter={}){
    let filterData = {}
  
    Object.keys(filter).forEach(key => {
      let convertKey = filter[key]
      
      if(convertKey){
        // just convert the data field if normal value
        filterData[convertKey] = data[key];
        
      }else{
        let itemData = data[key]
  
        if(itemData && Array.isArray(itemData)){
          // do not expend if array
          filterData[key] = itemData;
  
        }else if(typeof itemData == 'object'){
          // expend the data if data object except array
          filterData = { ...itemData, ...filterData }
  
        }else{
          // else normal binding
          filterData[key] = itemData;
        }
      }
  
    })
    return { ...filterData };
  }
  