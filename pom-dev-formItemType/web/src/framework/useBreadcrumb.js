import { useContext } from 'react';
import { useDidMount, useWillUnmount } from 'zero-element/lib/utils/hooks/lifeCycle';
import GlobalContext from '@/framework/GlobalContext';

/**
 * 自定义 面包屑导航
 *
 * @export
 * @param {Array} breadcrumb 自定义的面包屑列表
 */
export default function useBreadcrumb(breadcrumb) {
  const { OnBreadcrumb, OnBreadcrumbClear } = useContext(GlobalContext);

  useDidMount(_ => {
    OnBreadcrumb(breadcrumb);
  });
  useWillUnmount(_ => {
    OnBreadcrumbClear();
  })
}