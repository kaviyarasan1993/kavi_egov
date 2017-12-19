var dat = {
  "legal.update": {
    numCols: 4,
    title:"referenceEvidence.update.document.title",
    useTimestamp: true,
    objectName: "cases",
    documentsPath:"cases[0].referenceEvidences[0]",
    searchUrl:
      "/lcms-services/legalcase/case/_search?code={id}",
    groups: [
      {
        name: "addReferenceEvidences",
        label: "referenceEvidence.create.group.title.addReferenceEvidences",
        fields: [
          {
            name: "typeOfReference",
            jsonPath: "cases[0].referenceEvidences[0].referenceType",
            label: "referenceEvidence.create.typeOfReference",
            type: "text",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "referenceDate",
            jsonPath: "cases[0].referenceEvidences[0].referenceDate",
            label: "referenceEvidence.create.referenceDate",
            type: "datePicker",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "caseNo",
            jsonPath: "cases[0].summon.caseNo",
            label: "referenceEvidence.create.caseNo",
            type: "text",
            isRequired: true,
            isDisabled: true,
            url:
              "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=year|$..code|$..name",
            patternErrorMsg: ""
          },{
            name: "referenceCaseNo",
            jsonPath: "cases[0].referenceEvidences[0].referenceCaseNo",
            label: "referenceEvidence.create.referenceCaseNo",
            type: "text",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },{
            name: "description",
            jsonPath: "cases[0].referenceEvidences[0].description",
            label: "referenceEvidence.create.description",
            type: "textarea",
            fullWidth: true,
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: ""
          } 
          
        ]
      }, {
        name: "UploadDocument",
        label: "legal.create.group.title.UploadDocument",
        fields: [
          {
            name: "UploadDocument",
            jsonPath: "cases[0].referenceEvidences[0].documents",
            label: "legal.create.sectionApplied",
            type: "fileTable",
            isRequired: false,
            isDisabled: false,
            patternErrMsg: "",
            fileList: {
              name: "documentName",
              id: "fileStoreId"
            },
            fileCount: 3
          }
        ]
      }
    ],
    url:
      "/lcms-services/legalcase/referenceevidence/_create",
    tenantIdRequired: true
  }
};
export default dat;
