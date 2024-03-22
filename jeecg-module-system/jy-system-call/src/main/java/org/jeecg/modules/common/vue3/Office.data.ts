import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '父id',
    align:"center",
    dataIndex: 'parentId'
   },
   {
    title: '科室名称',
    align:"center",
    dataIndex: 'officeName'
   },
   {
    title: '科室简介',
    align:"center",
    dataIndex: 'officeDescription'
   },
   {
    title: '租户号',
    align:"center",
    dataIndex: 'tenantId'
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '父id',
    field: 'parentId',
    component: 'Input',
  },
  {
    label: '科室名称',
    field: 'officeName',
    component: 'Input',
  },
  {
    label: '科室简介',
    field: 'officeDescription',
    component: 'Input',
  },
  {
    label: '租户号',
    field: 'tenantId',
    component: 'InputNumber',
  },
	// TODO 主键隐藏字段，目前写死为ID
	{
	  label: '',
	  field: 'id',
	  component: 'Input',
	  show: false
	},
];



/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}