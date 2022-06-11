import React from 'react';

export default function SubMenu({ collaps, selected, title, children }) {
  const classes = [
    'nav-item-container',
    collaps ? 'collaps' : '',
    selected ? 'selected' : '',
  ].join(' ');

  return <div className={classes}>
    <div>{title}</div>
    <div>{children}</div>
  </div>
}