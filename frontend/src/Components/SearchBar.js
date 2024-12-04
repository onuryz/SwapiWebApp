import React from 'react';
import { Row, Col, Select, Input } from 'antd';

const { Option } = Select;

const SearchBar = ({ filterField, filterValue, onFieldChange, onValueChange, searchFields }) => (
  <Row gutter={16} style={{ marginBottom: 16 }}>
    <Col span={8}>
      <Select
        style={{ width: '100%' }}
        placeholder="Select Field"
        onChange={onFieldChange}
        value={filterField}
      >
        {searchFields.map((field) => (
          <Option key={field.value} value={field.value}>
            {field.label}
          </Option>
        ))}
      </Select>
    </Col>
    <Col span={16}>
      <Input
        placeholder="Enter search value"
        value={filterValue}
        onChange={(e) => onValueChange(e.target.value)}
        allowClear
      />
    </Col>
  </Row>
);

export default SearchBar;
