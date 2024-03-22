import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '预约值班医生详情Id;',
    align:"center",
    dataIndex: 'doctorWorkDetailId'
   },
   {
    title: '患者就诊卡id',
    align:"center",
    dataIndex: 'cardId'
   },
   {
    title: '医生Id;',
    align:"center",
    dataIndex: 'doctorId'
   },
   {
    title: '排队号;',
    align:"center",
    dataIndex: 'queueNumber'
   },
   {
    title: '预约号',
    align:"center",
    dataIndex: 'appointNumber'
   },
   {
    title: '预约日期',
    align:"center",
    dataIndex: 'appointDay'
   },
   {
    title: '预约时段',
    align:"center",
    dataIndex: 'appointPeriod'
   },
   {
    title: '预约状态（预约状态 0：预约中，1：成功，2：失败，3：取消，4：爽约）',
    align:"center",
    dataIndex: 'appointStatus'
   },
   {
    title: '就诊状态（预约状态 0：未就诊，1：就诊中，2：就诊完成）;',
    align:"center",
    dataIndex: 'clinicStatus'
   },
   {
    title: '就诊开始时间',
    align:"center",
    dataIndex: 'clinicBeginTime'
   },
   {
    title: '就诊结束时间',
    align:"center",
    dataIndex: 'clinicEndTime'
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
    label: '预约值班医生详情Id;',
    field: 'doctorWorkDetailId',
    component: 'Input',
  },
  {
    label: '患者就诊卡id',
    field: 'cardId',
    component: 'Input',
  },
  {
    label: '医生Id;',
    field: 'doctorId',
    component: 'Input',
  },
  {
    label: '排队号;',
    field: 'queueNumber',
    component: 'InputNumber',
  },
  {
    label: '预约号',
    field: 'appointNumber',
    component: 'InputNumber',
  },
  {
    label: '预约日期',
    field: 'appointDay',
    component: 'DatePicker',
    componentProps: {
       showTime: true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
  },
  {
    label: '预约时段',
    field: 'appointPeriod',
    component: 'Input',
  },
  {
    label: '预约状态（预约状态 0：预约中，1：成功，2：失败，3：取消，4：爽约）',
    field: 'appointStatus',
    component: 'InputNumber',
  },
  {
    label: '就诊状态（预约状态 0：未就诊，1：就诊中，2：就诊完成）;',
    field: 'clinicStatus',
    component: 'InputNumber',
  },
  {
    label: '就诊开始时间',
    field: 'clinicBeginTime',
    component: 'DatePicker',
    componentProps: {
       showTime: true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
  },
  {
    label: '就诊结束时间',
    field: 'clinicEndTime',
    component: 'DatePicker',
    componentProps: {
       showTime: true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
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