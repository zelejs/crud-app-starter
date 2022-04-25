import React from 'react';

const useLayout = require('@/components/hooks/useLayout');
export default function Container ({ children, ...rest }) {
  const [layoutRef, { getClassName }] = useLayout();

  return <div
    className={getClassName()}
  >
    {React.Children.toArray(children).map(child => {
      return React.cloneElement(child, {
        ref: layoutRef,
        ...rest
      })
    })}
  </div>
}


// export default function Container(props) {
//   const {children,...data} = props

//   return React.Children.map(children, child => {
//     return <div>
//       {child}
//     </div>
//   })
// }
