from flask_restful import Api
import resources.category as CategoryResource

def categoryRouteIndex(app):
    api = Api(app)
    
    api.add_resource(CategoryResource.CreateCategory, '/api/category/create')
    api.add_resource(CategoryResource.ReadAllCategory, '/api/category/read')
    api.add_resource(CategoryResource.UpdateCategory, '/api/category/update')
    api.add_resource(CategoryResource.DeleteCategory, '/api/category/delete')