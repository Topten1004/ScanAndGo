from models.category import CategoryModel
from flask_restful import Resource, reqparse
from flask import request

parser = reqparse.RequestParser()
parser.add_argument('id')
parser.add_argument('name')

class CreateCategory(Resource):
    def post(self):
        data = parser.parse_args()
        
        if (CategoryModel.find_by_name(data['name'])):
            return {'status': 0}
        
        new_item = CategoryModel(
            name = data['name']
        )
        try:
            new_item.save_to_db()
            return { 'status': 1 }, 200
        except:
            return {'status': -1}, 400

class ReadCategory(Resource):
    def get(self):
        return CategoryModel.return_all_by_id(request.args.get('id'))

class ReadAllCategory(Resource):
    def get(self):
        return CategoryModel.return_all()
    
class DeleteCategory(Resource):
    def delete(self):
        return CategoryModel.delete_one(request.args.get('id'))
    
class UpdateCategory(Resource):
    def put(self):
        data = parser.parse_args()
        return CategoryModel.update_one(request.args.get('id'), data['name']) 