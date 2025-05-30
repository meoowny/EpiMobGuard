import request from "@/utils/request";

// 查询角色列表
export function listRole(data) {
  return request({
    url: "/coverage/list",
    method: "get",
    params: data,
  });
}
// 查询角色列表
export function getListByType(data) {
  return request({
    url: "/coverage/listByType",
    method: "get",
    params: data,
  });
}

// 查询角色列表
export function listRoleCverg(data) {
  return request({
    url: "/coverage/listForCoverage",
    method: "get",
    params: data,
  });
}

// 查询角色详细
export function getRole(data) {
  return request({
    url: "/city/get",
    method: "get",
    params: data,
  });
}

// 新增角色
export function addRole(data) {
  return request({
    url: "/coverage/add",
    method: "post",
    data: data,
  });
}

// 修改角色
export function publisher(data) {
  return request({
    url: "/coverage/publisher",
    method: "post",
    data: data,
  });
}
// 增加在线图层
export function invoking(data) {
  return request({
    url: "/calculate/invoking",
    method: "post",
    data: data,
  });
}

// 修改角色
export function updateRole(data) {
  return request({
    url: "/city/edit",
    method: "post",
    data: data,
  });
}

// 角色数据权限
export function dataScope(data) {
  return request({
    url: "/system/role/dataScope",
    method: "put",
    data: data,
  });
}

// 角色状态修改
export function changeRoleStatus(roleId, status) {
  const data = {
    roleId,
    status,
  };
  return request({
    url: "/system/role/changeStatus",
    method: "put",
    data: data,
  });
}

// 删除角色
export function delRole(data) {
  return request({
    url: "/coverage/delete",
    method: "post",
    data: data,
  });
}

// 删除角色
export function cityList(data) {
  return request({
    url: "/city/listAll",
    method: "get",
    params: data,
  });
}

// 删除角色
export function typeListUp(data) {
  return request({
    url: "/dataType/listAll",
    method: "get",
    params: data,
  });
}

export function dataFile(id) {
  return request({
    url: `/system/planning/${id}`,
    method: "get",
  });
}

// 查询角色未授权用户列表
export function unallocatedUserList(query) {
  return request({
    url: "/system/role/authUser/unallocatedList",
    method: "get",
    params: query,
  });
}

// 取消用户授权角色
export function authUserCancel(data) {
  return request({
    url: "/system/role/authUser/cancel",
    method: "put",
    data: data,
  });
}

// 批量取消用户授权角色
export function authUserCancelAll(data) {
  return request({
    url: "/system/role/authUser/cancelAll",
    method: "put",
    params: data,
  });
}

// 授权用户选择
export function getFile() {
  return request({
    url: "/coverage/getFile",
    method: "get",
    responseType: 'blob'
  });
}

export function downloadFile(data) {
  return request({
    url: "/coverage/downloadFile",
    method: "get",
    params: data,
    responseType: 'blob'
  });
}

export function downloadFileById(data) {
  return request({
    url: "/coverage/downloadFileById",
    method: "get",
    params: data,
    responseType: 'blob'
  });
}

// 新增角色
export function addOtherFile(data) {
  return request({
    url: "/coverage/addOtherFile",
    method: "post",
    data: data,
  });
}
