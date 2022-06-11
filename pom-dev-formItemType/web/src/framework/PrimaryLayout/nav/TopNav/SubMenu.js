import React from 'react';

export default function SubMenu({ selected, title, children }) {
  const classes = [
    'nav-item-container',
    selected ? 'selected' : '',
  ].join(' ');

  return <div className={classes}>
    <div>{title}</div>
    <div>{children}</div>
  </div>
}