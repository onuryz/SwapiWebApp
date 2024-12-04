import React, { useState } from 'react';
import { Layout, Tabs } from 'antd';

import HeaderView from './Components/HeaderView';
import PeopleTab from './Pages/PeopleTab';
import PlanetsTab from './Pages/PlanetsTab';
import './App.css';

const { Header, Content } = Layout;

const App = () => {
  const [activeTab, setActiveTab] = useState('1');

  const handleTabChange = (key) => {
    setActiveTab(key);
  };

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Header>
        <HeaderView />
      </Header>
      <Content style={{ margin: '15px' }}>
        <Tabs
          activeKey={activeTab}
          onChange={(key) => handleTabChange(key)}
          style={{
            backgroundColor: 'white',
            padding: '15px',
            borderRadius: '8px',
            height: '100%',
            boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
          }}
          tabBarStyle={{ marginBottom: 15 }}
        >
          <Tabs.TabPane tab="People" key="1">
            <PeopleTab />
          </Tabs.TabPane>
          <Tabs.TabPane tab="Planets" key="2">
            <PlanetsTab />
          </Tabs.TabPane>
        </Tabs>
      </Content>
    </Layout>
  );
};

export default App;
