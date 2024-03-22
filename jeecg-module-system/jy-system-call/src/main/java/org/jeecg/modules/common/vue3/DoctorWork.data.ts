import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '医生id',
    align:"center",
    dataIndex: 'doctorId'
   },
   {
    title: '就诊室id;',
    align:"center",
    dataIndex: 'clinicRoomId'
   },
   {
    title: '科室id;',
    align:"center",
    dataIndex: 'officeId'
   },
   {
    title: '出诊日期',
    align:"center",
    dataIndex: 'workDay'
   },
   {
    title: '开始预约时间',
    align:"center",
    dataIndex: 'appointTime'
   },
   {
    title: '号源总数',
    align:"center",
    dataIndex: 'numTotal'
   },
   {
    title: '预约类型（1：时段，2：排队）;',
    align:"center",
    dataIndex: 'appointType'
   },
   {
    title: '已预约总数',
    align:"center",
    dataIndex: 'appointTotal'
   },
   {
    title: '预约状态（0：未开始、1：预约中、2：已满，3：预约结束）',
    align:"center",
    dataIndex: 'appointStatus'
   },
   {
    title: '出诊状态（0：未开始、1：出诊中、2：就诊完成，3：停诊）',
    align:"center",
    dataIndex: 'clinicStatus'
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
    label: '医生id',
    field: 'doctorId',
    component: 'Input',
  },
  {
    label: '就诊室id;',
    field: 'clinicRoomId',
    component: 'Input',
  },
  {
    label: '科室id;',
    field: 'officeId',
    component: 'Input',
  },
  {
    label: '出诊日期',
    field: 'workDay',
    component: 'DatePicker',
    componentProps: {
       showTime: true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
  },
  {
    label: '开始预约时间',
    field: 'appointTime',
    component: 'DatePicker',
    componentProps: {
       showTime: true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
  },
  {
    label: '号源总数',
    field: 'numTotal',
    component: 'InputNumber',
  },
  {
    label: '预约类型（1：时段，2：排队）;',
    field: 'appointType',
    component: 'InputNumber',
  },
  {
    label: '已预约总数',
    field: 'appointTotal',
    component: 'InputNumber',
  },
  {
    label: '预约状态（0：未开始、1：预约中、2：已满，3：预约结束）',
    field: 'appointStatus',
    component: 'InputNumber',
  },
  {
    label: '出诊状态（0：未开始、1：出诊中、2：就诊完成，3：停诊）',
    field: 'clinicStatus',
    component: 'InputNumber',
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