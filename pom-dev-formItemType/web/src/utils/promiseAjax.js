
import { getToken } from 'zero-element/lib/utils/request/token';

export default (url, data, options = {}) => {
    const { method = 'GET', async = true, token = getToken() } = options;
  
    let param = '';
    let payload = {};
    if (method === 'GET') {
      if(data && JSON.stringify(data) != "{}"){
        param = `?${Object.keys(data).map(key => `${key}=${data[key]}`).join('&')}`;
      }
    } else {
      if (data) {
          payload = JSON.stringify(data);
      }
    }
  
    return new Promise((resolve, reject) => {
      let xhr = new XMLHttpRequest();
      xhr.open(method, `${url}${param}`, async);
      xhr.responseType = 'JSON';

      xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
      
      if(token){
        xhr.setRequestHeader("Authorization", `Bearer ${token}`);
      }
  
      xhr.onreadystatechange = () => {
  
        if (xhr.readyState !== 4) {
          return;
        }
  
        if (xhr.readyState === 4 && xhr.status === 200) {
          let result
          try {
            result = JSON.parse(xhr.responseText);
            resolve(result);
  
          } catch (error) {
            reject("返回的数据非 json 格式");
          }
        } else {
          reject(xhr.statusText);
        }
      }
      xhr.onerror = (err) => {
        reject(err);
      }
  
      xhr.send(payload);
    })
} 