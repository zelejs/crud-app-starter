import React from 'react';

import Container from '@/components/container/Container';
import Flexbox from '@/components/layout/Flexbox1';

export default function Round (props) {

  const { children, width } = props;

  return (
    <>
      <Container>
        <Flexbox align='center'>
          {children}
          {width}
        </Flexbox>
      </Container>
    </>
  )
}