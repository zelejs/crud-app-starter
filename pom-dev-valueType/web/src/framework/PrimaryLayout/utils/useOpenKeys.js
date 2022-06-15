import { useState } from 'react';

export default function useOpenKeys() {
  const [openKeys, setOpendKeys] = useState([]);

  function handleClick(key) {
    setOpendKeys(key);
  }
  return [openKeys, handleClick];
}