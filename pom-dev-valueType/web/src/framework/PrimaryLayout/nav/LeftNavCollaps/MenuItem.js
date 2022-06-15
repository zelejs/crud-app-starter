import React from 'react';

export default function MenuItem({ collaps, selected, children }) {
  const classes = [
    'nav-item',
    collaps ? 'collaps' : '',
    selected ? 'selected' : '',
  ].join(' ');

  return <div className={classes}>
    <div>{children}</div>
  </div>
}