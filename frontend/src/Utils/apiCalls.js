import axios from 'axios';

export const fetchPeopleData = async (filterField, filterValue, sortField, sortOrder, page, pageSize) => {
  const url = `http://localhost:6970/people`;
  const params = {
    filterField,
    filterValue,
    sort: sortField,
    order: sortOrder,
    page,
    size: pageSize
  }
  const response = await axios.get(url, {params});
  return response.data;
};

export const fetchPlanetsData = async (filterField, filterValue, sortField, sortOrder, page, pageSize) => {
  const url = `http://localhost:6970/planets`;
  const params = {
    filterField,
    filterValue,
    sort: sortField,
    order: sortOrder,
    page,
    size: pageSize
  }
  const response = await axios.get(url, {params});
  return response.data;
};

function timeout(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}
