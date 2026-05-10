TRUNCATE TABLE notification_tasks, trips, drivers, passengers
RESTART IDENTITY CASCADE;

SELECT 'Database cleaned successfully. IDs reset to 1.' AS status;