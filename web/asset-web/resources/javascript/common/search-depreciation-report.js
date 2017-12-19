var flag = 0;
class SearchDepreciationReport extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],searchSet:{
      "tenantId": tenantId,
      "assetName": "",
      "assetCode":"",
      "assetCategory": "",
      "assetCategoryType": "",
      "parent":"",
      "department": "",
      "financialYear":""
   },isSearchClicked:false,asset_category_type:[],assetCategories:[],departments:[],result:[]}
    this.handleChange = this.handleChange.bind(this);
    this.search = this.search.bind(this);
    //this.handleClick = this.handleClick.bind(this);
    this.setInitialState = this.setInitialState.bind(this);
  }

  handleChange(e, name) {
    var self = this;
      if(name === 'assetCategoryType'){
        commonApiPost("asset-services", "assetCategories", "_search", {assetCategoryType:e.target.value,tenantId}, function(err, res) {
            let parentCategory = res["AssetCategory"].filter((obj)=>{
              return !obj.parent;//false
            })
            self.setState({parentCategories:parentCategory})
        })
      }else if(name === 'parent'){
        commonApiPost("asset-services", "assetCategories", "_search", {parent:e.target.value,tenantId}, function(err, res) {
            console.log(res["AssetCategory"]);
            let assetCategory = res["AssetCategory"].filter((obj)=>{
              return obj.parent;//false
            })
            self.setState({assetCategories:assetCategory});
        })
      }

      if(name == "assetName") {
        return this.setState({
          searchSet: {
              ...this.state.searchSet,
              [name]: e.target.value,
              asset: ""
          }
      })
      }

      if(name == "assetCode") {
        return this.setState({
          searchSet: {
              ...this.state.searchSet,
              [name]: e.target.value,
              asset: ""
          }
      })
      }
      this.setState({
          searchSet:{
              ...this.state.searchSet,
              [name]:e.target.value
          }
      })
  }

  setInitialState(initState) {
    this.setState(initState);
  }

  search(e) {
    e.preventDefault();
    try {
      //call api call
      var _this = this;
      var searchSet = Object.assign({}, this.state.searchSet);
      if(searchSet.asset)
        delete searchSet.allotteeName;
      else
        delete searchSet.asset;
      console.log(this.state.searchSet);
      commonApiPost("asset-services","assets/depreciations","_search", {...this.state.searchSet, tenantId, pageSize:500}, function(err, res) {
        if(res) {
          var list = res["DepreciationReportCriteria"];
          flag = 1;
          _this.setState({
            isSearchClicked: true,
            list
          })
        }
      })
    } catch(e) {
      console.log(e);
    }
  }

  componentDidMount() {
    if(window.opener && window.opener.document) {
      var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
      if(logo_ele && logo_ele[0]) {
        document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
      }
    }
    $('#hpCitizenTitle').text(titleCase(getUrlVars()["type"]) + " Asset");
    var count =0 , _this = this, _state = {};
    var checkCountNCall = function(key, res) {
      _state[key] = res;
      console.log(res);
      if(count == 0)
        _this.setInitialState(_state);
    }

    getDropdown("asset_category_type", function(res) {
      checkCountNCall("asset_category_type", res);
    });

     var location;

     $( "#assetName" ).autocomplete({
       source: function( request, response ) {
         $.ajax({
           url: baseUrl + "/asset-services/assets/_search?tenantId=" + tenantId,
           type: 'POST',
           dataType: "json",
           data: JSON.stringify({
               RequestInfo: requestInfo,
               name: request.term,
               fuzzyLogic: true,
               tenantId: tenantId
           }),
           contentType: 'application/json',
           success: function( data ) {
             if(data && data.Assets && data.Assets.length) {
                 let users = [];
                 for(let i=0;i<data.Assets.length;i++)
                     users.push(data.Assets[i].name);
                 response(users);
                 _this.setState({
                   users: data.Assets
                 })
             }
           }
         });
       },
       minLength: 3,
       change: function( event, ui ) {
         if(ui.item && ui.item.value) {
             var id;
             if(_this.state.users && _this.state.users.constructor == Array) {
               for(var i=0; i<_this.state.users.length; i++) {
                 if(_this.state.users[i].name == ui.item.value) {
                   id = _this.state.users[i].id;
                 }
               }
             }

             _this.setState({
                 searchSet:{
                     ..._this.state.searchSet,
                     assetName: ui.item.value,
                     asset: id || ""
                 }
             })
         }
       }
     });

     $( "#assetCode" ).autocomplete({
       source: function( request, response ) {
         $.ajax({
           url: baseUrl + "/asset-services/assets/_search?tenantId=" + tenantId,
           type: 'POST',
           dataType: "json",
           data: JSON.stringify({
               RequestInfo: requestInfo,
               name: request.term,
               fuzzyLogic: true,
               tenantId: tenantId
           }),
           contentType: 'application/json',
           success: function( data ) {
             if(data && data.Assets && data.Assets.length) {
                 let users = [];
                 for(let i=0;i<data.Assets.length;i++)
                     users.push(data.Assets[i].code);
                 response(users);
                 _this.setState({
                   users: data.Assets
                 })
             }
           }
         });
       },
       minLength: 3,
       change: function( event, ui ) {
         if(ui.item && ui.item.value) {
             var id;
             if(_this.state.users && _this.state.users.constructor == Array) {
               for(var i=0; i<_this.state.users.length; i++) {
                 if(_this.state.users[i].code == ui.item.value) {
                   id = _this.state.users[i].id;
                 }
               }
             }

             _this.setState({
                 searchSet:{
                     ..._this.state.searchSet,
                     assetCode: ui.item.value,
                     asset: id || ""
                 }
             })
         }
       }
     });




}

close() {
    open(location, '_self').close();
}

//Fetch asset  name suggestions

  render() {
    let {handleChange, search, handleClick}=this;
    let {assetCategoryType,assetCategory,financialYear,department,parent,assetName,assetCode}=this.state.searchSet;
    let {isSearchClicked,list,departments,assetCategories}=this.state;
    console.log(assetCategories);
      const renderOption = function(list) {
          if(list) {

              if (list.length) {
                list.sort(function(item1, item2) {
                  if(item1.name && item2.name)
                    return item1.name.toLowerCase() > item2.name.toLowerCase() ? 1 : item1.name.toLowerCase() < item2.name.toLowerCase() ? -1 : 0;
                  else
                    return 0;
                });

                return list.map((item)=> {
                    return (<option key={item.id} value={item.id}>
                            {item.name}
                      </option>)
                })

              } else {
                return Object.keys(list).map((k, index)=>
                {
                  return (<option key={index} value={k}>
                          {list[k]}
                    </option>)

                 })
              }

          }
      }

      const showTable = function() {
        if(isSearchClicked)
        {
            return (
              <table id="searchDepreciationTable" className="table table-bordered">
                  <thead>
                  <tr>
                      <th>Sr. No.</th>
                      <th>Asset Code</th>
                      <th>Asset Name</th>
                      <th>Asset Category Name</th>
                      <th>Department</th>
                      <th>Asset Category Type</th>
                      <th>Depreciation Rate(%)</th>
                      <th>Gross Value(Rs.)</th>
                      <th>Current Depreciation(Rs.)</th>
                      <th>Value After Depreciation(Rs.)</th>


                  </tr>
                  </thead>
                  <tbody id="depreciationSearchResultTableBody">
                          {
                              renderBody()
                          }
                      </tbody>

             </table>
            )
        }
    }

    const renderBody = function() {
      if (list.length>0) {
         ////console.log("list",list);
        return list.map((item,index)=>
        {
                  return (<tr key={index} onClick={() => {handleClick(getUrlVars()["type"], item.id)}}>
                        <td>{index+1}</td>
                        <td>{item.assetCode}</td>
                        <td>{item.assetName}</td>
                        <td>{item.assetCategoryName}</td>
                        <td>{getNameById(departments,item.department.id)}</td>
                        <td>{item.assetCategoryType}</td>
                        <td>{item.depreciationRate}</td>
                        <td>{item.grossValue}</td>
                        <td>{item.depreciationValue}</td>
                        <td>{item.valueAfterDepreciation}</td>




                  </tr>  );
        })
      }
    }

    return (<div>
      <h3>{titleCase(getUrlVars()["type"])} Depreciation Report</h3>
      <div className="form-section-inner">
        <form onSubmit={(e)=>{search(e)}}>
          <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for="assetName"> Asset Name</label>
                  </div>
                  <div className="col-sm-6">
                    <input id="assetName" name="assetName" value={assetName} type="text"
                      onChange={(e)=>{handleChange(e,"assetName")}}/>
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for="assetCode"> Asset Code</label>
                  </div>
                  <div className="col-sm-6">
                    <input id="assetCode" name="assetCode" value={assetCode} type="text"
                      onChange={(e)=>{handleChange(e,"assetCode")}}/>
                  </div>
                </div>
            </div>
              <div className="row">
                <label className="col-sm-3 control-label text-right" > Financial Year<span> *</span></label>
                <div className="col-sm-3 add-margin">
                  <select id="financialYear" name="financialYear" className="form-control" required= "true" onChange={(e)=>{handleChange(e,"financialYear")}}>
                    <option value="">Select</option>
                    <option value="2016-17">2016-17</option>
                    <option value="2017-18">2017-18</option>
                  </select>
                </div>
                </div>
              <div className="col-sm-6">
                <div className="row">
                    <div className="col-sm-6 label-text">
                        <label for="assetCategoryType">Asset Category Type <span> *</span></label>
                    </div>
                    <div className="col-sm-6">
                      <div className="styled-select">
                        <select id="assetCategoryType" name="assetCategoryType" value={assetCategoryType} required= "true" onChange={(e)=>{
                        handleChange(e,"assetCategoryType")}}>
                            <option value="">Select Asset Category Type</option>
                            {renderOption(this.state.asset_category_type)}
                          </select>
                      </div>
                </div>
            </div>
          </div>
            <div className="col-sm-6">
              <div className="row">
                <div className="col-sm-6 label-text">
                  <label for="parent"> Parent Category  </label>
                </div>
                <div className="col-sm-6">
                  <div className="styled-select">
                    <select  name="parent" value={parent} onChange={(e)=>{
                        handleChange(e,"parent")}}>
                        <option value="">Select Parent Category</option>
                          {renderOption(this.state.parentCategories)}
                    </select>
                  </div>
                </div>
              </div>
            </div>
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                        <label for="assetCategory">Asset Category </label>
                      </div>
                    <div className="col-sm-6">
                       <div className="styled-select">
                       <select id="assetCategory" name="assetCategory" value={assetCategory} onChange={(e)=>{
                       handleChange(e,"assetCategory")}}>
                           <option value="">Select Asset Category</option>
                           {renderOption(this.state.assetCategories)}
                         </select>
                    </div>
                  </div>
                </div>
              </div>
              </div>
              <div className="text-center">
                  <button type="submit" className="btn btn-submit">Search</button>&nbsp;&nbsp;
                  <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>
              </div>

              </form>
              </div>
              <div className="table-cont" id="table">
                  {showTable()}
              </div>
          </div>
        );
    }
  }

ReactDOM.render(
  <SearchDepreciationReport />,
  document.getElementById('root')
);
