import React from 'react';
import { Typography } from 'antd';

const { Title } = Typography;

const HeaderView = ({ filterField, filterValue, onFieldChange, onValueChange, searchFields }) => (
  <div className="headerView">
    <Title style={{ color: 'white', margin: 0, textAlign: 'center' }} level={2}>
      SWAPI Web App
    </Title>
  </div>
);

export default HeaderView;
