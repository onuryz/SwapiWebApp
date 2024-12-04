import React from 'react';
import { Table } from 'antd';
import { useMediaQuery } from 'react-responsive';

const TableView = ({ data, columns, onTableChange, tablePagination }) => {
  const isSmallScreen = useMediaQuery({ maxWidth: 768 });

  return (
    <Table
      dataSource={data}
      columns={columns}
      rowKey="name"
      pagination={{
        current: tablePagination.currentPage,
        pageSize: tablePagination.pageSize,
        total: tablePagination.total,
        showSizeChanger: false
      }}
      onChange={onTableChange}
      size="small"
      scroll={isSmallScreen ? { x: 'max-content' } : undefined} 
    />
  )
};

export default TableView;
