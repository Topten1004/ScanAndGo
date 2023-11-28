from models.detailLocation import DetailLocationModel
from flask_restful import Resource, reqparse
from flask import request

parser = reqparse.RequestParser()
parser.add_argument('id')
parser.add_argument('floorId')
parser.add_argument('name')
parser.add_argument('imgData')

class CreateDetailLocation(Resource):
    def post(self):
        data = parser.parse_args()
        
        new_item = DetailLocationModel(
            floorId = data['floorId'],
            name = data['name'],
            img_data = data['imgData']
        )
        try:
            new_item.save_to_db()
            return { 'message': 'created!' }, 200
        except Exception as e:
            return {'message': str(e)}, 400

class ReadDetailLocation(Resource):
    def get(self):
        return DetailLocationModel.return_all_by_id(request.args.get('id'))
    
class DeleteDetailLocation(Resource):
    def delete(self):
        return DetailLocationModel.delete_one(request.args.get('id'))
    
class UpdateDetailLocation(Resource):
    def put(self):
        data = parser.parse_args()
        return DetailLocationModel.update_one(request.args.get('id'), data['name'])
    
class ReadAllDetailLocation(Resource):
    def get(self):
        return DetailLocationModel.return_all()