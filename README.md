# Warehouse Management Software

## Description

This project is a warehouse management software application 
implemented in Java 17 using spring boot v3.3.1. 

The software manages articles and products.

Articles have an ID, name, and available stock. Products are 
composed of various articles. 
The software supports loading articles and products from JSON 
files, performing operations like retrieving all available 
products with their quantities available within the current inventory,
and updating the inventory when products are sold.

## Requirements
    Java 17
    Maven
    Spring Boot
    Docker
    MySQL DB
    

## Running the Application Locally
    
1. Clone this repository

        git clone https://github.com/shamanth09/warehouse.git

2. Run the below maven command in the root directory of the project
        
        mvn clean install    

3. Open command prompt and run below docker command 
   
        docker compose up

## Limitation 

1. Product name(s) should be unique. To avoid confusion in producing product using articles.

## Further Implementations

1. Implement security measures to protect the application.
2. Update the articles associated with a product.
3. Alert users if duplicate products are identified during subsequent product requests.
4. Documentation REST APIs using Swagger.
    
        