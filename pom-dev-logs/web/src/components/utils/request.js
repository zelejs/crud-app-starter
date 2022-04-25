
const { getEndpoint, getToken } = require('@/components/config/common');

module.exports = function promiseAjax(url, data = {}, options = {}) {
  const { method = 'GET', async = true, token=getToken() } = options;

  let param = '';
  let payload;
  if (method === 'GET') {
    if(data && JSON.stringify(data) != '{}'){
      param = `?${Object.keys(data).map(key => `${key}=${data[key]}`).join('&')}`;
    }
  } else {
    payload = JSON.stringify(data);
  }

  return new Promise((resolve, reject) => {
    let xhr = new XMLHttpRequest();
    xhr.open(method, `${getEndpoint()}${url}${param}`, async);

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

    if(method === 'POST' || method === 'PUT'){
      xhr.setRequestHeader("Content-Type", "application/json")
    }

    xhr.onerror = (err) => {
      reject(err);
    }

    xhr.send(payload);
  })
}