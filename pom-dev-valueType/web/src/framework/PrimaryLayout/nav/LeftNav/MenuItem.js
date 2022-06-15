import React from 'react';

export default function MenuItem({ selected, children }) {
  const classes = [
    'nav-item',
    selected ? 'selected' : '',
  ].join(' ');

  return <div className={classes}>
    <div>{children}</div>
  </div>
}