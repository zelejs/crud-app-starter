import global, { set as golbalSet } from 'zero-element/lib/config/global';
import { set as setEndpoint, get as getEndPoint } from 'zero-element/lib/utils/request/endpoint';

export default function () {
  const { tempEndpoint } = global;
  if (process.env.NODE_ENV === 'development' && tempEndpoint) {
    golbalSet({
      tempEndpoint: getEndPoint(),
    });
    setEndpoint(tempEndpoint);
  }
}