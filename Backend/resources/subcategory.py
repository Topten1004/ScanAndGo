from models.subcategory import SubCategoryModel
from flask_restful import Resource, reqparse
from flask import request

parser = reqparse.RequestParser()
parser.add_argument('id')
parser.add_argument('categoryId')
parser.add_argument('name')

class CreateSubCategory(Resource):
    def post(self):
        data = parser.parse_args()
        
        new_item = SubCategoryModel(
            category_id = data['categoryId'],
            name = data['name']
        )
        try:
            new_item.save_to_db()
            return { 'message': 'created!' }, 200
        except:
            return {'message': 'Not created!'}, 400

class ReadSubCategory(Resource):
    def get(self):
        return SubCategoryModel.return_all_by_id(request.args.get('id'))
    
class DeleteSubCategory(Resource):
    def delete(self):
        return SubCategoryModel.delete_one(request.args.get('id'))
    
class UpdateSubCategory(Resource):
    def put(self):
        data = parser.parse_args()
        return SubCategoryModel.update_one(request.args.get('id'), data['name'])