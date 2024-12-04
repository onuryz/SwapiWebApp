import React, { useState, useEffect, useCallback } from 'react';
import { Spin, notification } from 'antd';
import debounce from 'lodash/debounce';

import SearchBar from '../Components/SearchBar';
import TableView from '../Components/TableView';
import { fetchPlanetsData } from '../Utils/apiCalls';

const PlanetsTab = () => {
  const [planetsData, setPlanetsData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [filterField, setFilterField] = useState('name');
  const [filterValue, setFilterValue] = useState('');
  const [sortField, setSortField] = useState('');
  const [sortOrder, setSortOrder] = useState('');
  const [tablePagination, setTablePagination] = useState({
    currentPage: 1,
    pageSize: 15,
    total: 0,
  });
  

  const planetsColumns = [
    { title: 'Name', dataIndex: 'name', key: 'name', ellipsis: true, sorter: true, width: 120 },
    { title: 'Population', dataIndex: 'population', key: 'population', ellipsis: true, width: 120 },
    { title: 'Climate', dataIndex: 'climate', key: 'climate', ellipsis: true, width: 150 },
    { title: 'Terrain', dataIndex: 'terrain', key: 'terrain', ellipsis: true, width: 200 },
    { title: 'Rotation Period', dataIndex: 'rotation_period', key: 'rotation_period', ellipsis: true, width: 140 },
    { title: 'Orbital Period', dataIndex: 'orbital_period', key: 'orbital_period', ellipsis: true, width: 130 },
    { title: 'Diameter', dataIndex: 'diameter', key: 'diameter', ellipsis: true, width: 120 },
    { title: 'Gravity', dataIndex: 'gravity', key: 'gravity', ellipsis: true, width: 120 },
    { title: 'Surface Water', dataIndex: 'surface_water', key: 'surface_water', ellipsis: true, width: 150 },
    { title: 'Created', dataIndex: 'created', key: 'created', ellipsis: true, sorter: true, width: 180, render: (value) => new Date(value).toLocaleString() },
    { title: 'Edited', dataIndex: 'edited', key: 'edited', ellipsis: true, sorter: true, width: 180, render: (value) => new Date(value).toLocaleString() },
  ];

  const planetsSearchFields = [
    { label: 'Name', value: 'name' },
    { label: 'Climate', value: 'climate' },
    { label: 'Terrain', value: 'terrain' },
  ]

  const fetchPlanets = useCallback(
    debounce(async (filterField, filterValue, sortField, sortOrder, page, pageSize ) => {
      setLoading(true);
      try {
        console.log(`/api/planets?filterField=${filterField}&filterValue=${filterValue}&sort=${sortField}&order=${sortOrder}&page=${page}&size=${pageSize}`);
        const {currentPage, numberOfPages, data} = await fetchPlanetsData(filterField, filterValue, sortField, sortOrder, page, pageSize);
        setPlanetsData(data);
        setTablePagination({
          ...tablePagination,
          currentPage: currentPage > numberOfPages ? numberOfPages : currentPage,
          total: numberOfPages * pageSize
        });
      } catch (error) {
        setPlanetsData([]);
        notification.error({
          message: 'Fetch Error',
          description: 'Could not fetch people data.',
        });
      } finally {
        setLoading(false);
      }
    }, 800),
    []
  );

  useEffect(() => {
    fetchPlanets(filterField, filterValue, sortField, sortOrder, tablePagination.currentPage, tablePagination.pageSize);
  }, [filterValue, sortField, sortOrder, tablePagination.currentPage, tablePagination.pageSize]);

  const handleTableChange = (pagination, filters, sorter) => {
    const { current, pageSize } = pagination;
    if (current !== tablePagination.currentPage || pageSize !== tablePagination.pageSize) {
      setTablePagination({
        ...tablePagination,
        currentPage: current
      });
    }
    if (sorter.order) {
      setSortField(sorter.field);
      setSortOrder(sorter.order === 'ascend' ? 'asc' : 'desc');
    } else {
      setSortField('');
      setSortOrder('');
    }
  };

  const handleFilterFieldChange = (value) => {
    setFilterField(value);
    setFilterValue('');
  }

  return (
    <Spin spinning={loading}>
      <SearchBar
        filterField={filterField}
        filterValue={filterValue}
        onFieldChange={handleFilterFieldChange}
        onValueChange={setFilterValue}
        searchFields={planetsSearchFields}
      />
      <TableView
        data={planetsData}
        columns={planetsColumns}
        onTableChange={handleTableChange}
        tablePagination={tablePagination}
      />
    </Spin>
  );
};

export default PlanetsTab;
