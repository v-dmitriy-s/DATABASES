export const API = {
    product: process.env.NEXT_PUBLIC_PRODUCT_API || "http://localhost:8082",
    recommendation: process.env.NEXT_PUBLIC_RECOMMENDATION_API || "http://localhost:8084",
    order: process.env.NEXT_PUBLIC_ORDER_API || "http://localhost:8080",
    inventory: process.env.NEXT_PUBLIC_INVENTORY_API || "http://localhost:8081",
    userActivity: process.env.NEXT_PUBLIC_USER_ACTIVITY_API || "http://localhost:8083",
};
