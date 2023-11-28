from flask_restful import Api
import resources.subcategory as SubCategoryResource

def subcategoryRouteIndex(app):
    api = Api(app)
    
    api.add_resource(SubCategoryResource.CreateSubCategory, '/api/subcategory/create')
    api.add_resource(SubCategoryResource.ReadSubCategory, '/api/subcategory/read')
    api.add_resource(SubCategoryResource.UpdateSubCategory, '/api/subcategory/update')
    api.add_resource(SubCategoryResource.DeleteSubCategory, '/api/subcategory/delete')