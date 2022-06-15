function checkData(rspData, childrenField) {
  if (!rspData) return [];

  if (rspData.id) {
    return rspData;
  } else if (rspData[childrenField]) {
    return rspData[childrenField];
  }
  if (Array.isArray(rspData)) {
    return rspData;
  }

  return [];
}

let islast;

function formatToTreeData(data, options, pId) {
  const {
    id: idField,
    children: childrenField,
  } = options;
  const rst = [];

  const switchicon = <svg t="1621219824765" className="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2662" width="16" height="16"><path d="M974.99592797 774.77400863H49.00407203V220.36641817h857.08789887c38.11120672 0 69.04204115 30.93083443 69.04204115 69.04204115v485.36554931h-0.13808408zM627.71446098 220.36641817c0-63.9329301-51.78153086-115.71446098-115.71446098-115.71446098H49.00407203v115.71446098h578.71038895" fill="#CE9F06" p-id="2663"></path><path d="M164.718533 620.25792053V273.11453762h694.562934v347.14338291h-694.562934z" fill="#FFFFFF" p-id="2664"></path><path d="M974.99592797 848.09665633V398.49488435c0-31.89742302-25.95980747-57.85723049-57.85723049-57.85723048H106.86130252c-31.89742302 0-57.85723049 25.95980747-57.85723049 57.85723048v449.4636879c0 39.35396347 31.89742302 71.38947055 71.38947055 71.38947056h783.35099891c39.35396347 0.13808409 71.25138647-31.89742302 71.25138648-71.25138648z" fill="#FFCD2C" p-id="2665"></path></svg>;
  const fileicon = <svg t="1621219862707" className="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="3504" width="16" height="16"><path d="M642.56 61.952v237.568c0 17.92 14.336 32.256 32.256 32.256h233.984v536.576c0 17.92-14.336 32.256-32.256 32.256H142.336c-17.92 0-32.256-14.336-32.256-31.744V94.208c0-17.92 14.336-32.256 32.256-32.256h500.224zM287.744 242.176v90.112h266.24V242.176h-266.24z m0 179.712V512h443.904V421.888H287.744z m0 180.224v90.112h443.904v-90.112H287.744z m449.024-539.136l171.008 173.568c1.024 1.024 1.024 3.072 0 4.608-0.512 0.512-1.536 1.024-2.048 1.024h-142.336c-17.92 0-32.256-14.336-32.256-32.256V65.536c0-1.536 1.536-3.072 3.072-3.072s2.048 0 2.56 0.512z" fill="#1296db" p-id="3505"></path></svg>;

  if (Array.isArray(data)) {
    data.forEach(i => {
      if(i.children!=undefined){
        islast = false
      }else{
        islast = true
      }
      const item = {
        ...i,
        key: i[idField] ? i[idField] : `${pId}_${i.id}`,
        title: i.title || i.name || i[idField] || i.id,
        switcherIcon:islast?fileicon:switchicon
      };
      
      if (Array.isArray(item[childrenField])) {
        const children = item[childrenField];
        delete item[childrenField];
        item.children = formatToTreeData(children, {
          id: idField,
          children: childrenField,
        }, item.key);
      }
      rst.push(item);
    })
  } else {
    rst.push(data);
  }
  return rst;
}

function mergeData(rspData, childrenField, idField) {
  const data = checkData(rspData, childrenField);
  let rst = [];

  if (Array.isArray(data)) {
    rst = formatToTreeData(data, {
      id: idField,
      children: childrenField,
    });
    // islast=false
  } else {
    // 在只返回了一条数据的情况下, 未必是树结构
    rst = formatToTreeData(data[childrenField], {
      id: idField,
      children: childrenField,
    });
  }
  return rst;
};

export default mergeData;