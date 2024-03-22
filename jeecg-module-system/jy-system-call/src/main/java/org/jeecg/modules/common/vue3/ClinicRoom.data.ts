import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '诊所id',
    align:"center",
    dataIndex: 'clinicId'
   },
   {
    title: '科室id',
    align:"center",
    dataIndex: 'officeId'
   },
   {
    title: '诊室名称',
    align:"center",
    dataIndex: 'clinicRoomName'
   },
   {
    title: '诊室编号',
    align:"center",
    dataIndex: 'clinicRoomCode'
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
    label: '诊所id',
    field: 'clinicId',
    component: 'Input',
  },
  {
    label: '科室id',
    field: 'officeId',
    component: 'Input',
  },
  {
    label: '诊室名称',
    field: 'clinicRoomName',
    component: 'Input',
  },
  {
    label: '诊室编号',
    field: 'clinicRoomCode',
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