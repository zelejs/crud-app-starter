import { useState, useEffect } from 'react';

export default function useSelectedKeys(path) {
  const [selectedKeys, setSelectedKeys] = useState([]);

  useEffect(_ => {
    const rst = [path];
    const pathSplit = path.split('/');
    while (Array.isArray(pathSplit) && pathSplit.length > 2) {
      pathSplit.pop();
      rst.push(pathSplit.join('/'));
    }
    rst[0] = rst[0].replace(/[_-](add|edit|view)$/g, '');
    setSelectedKeys(rst);
  }, [path]);

  return selectedKeys;
}