export default function promiseAjax(url, data={}, token, options = {}) {
    const { method = 'GET', async = true } = options;
  
    let param;
    let payload;
    if (method === 'GET') {
      param = `?${Object.keys(data).map(key => `${key}=${data[key]}`).join('&')}`;
    } else {
      payload = data;
    }
  
    return new Promise((resolve, reject) => {
      let xhr = new XMLHttpRequest();
      xhr.open(method, `${url}${param}`, async);
  
      if (token) {
        xhr.setRequestHeader("Authorization", `Bearer ${token}`);
      }
  
      xhr.responseType = 'JSON';
  
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