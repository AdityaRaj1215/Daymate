# 📧 Email Reminder System - Setup Guide

## ✅ Implementation Complete

Phase 2: Email Reminder System has been successfully implemented!

### What Was Implemented:

1. **EmailService** (`service/EmailService.java`)
   - Sends task reminder emails to users
   - Handles email configuration gracefully
   - Includes task details in email body

2. **ReminderScheduler** (`scheduler/ReminderScheduler.java`)
   - Runs periodically (every 15 minutes by default)
   - Checks for tasks with due dates within 24 hours
   - Sends reminders only for incomplete tasks
   - Marks tasks as `reminderSent = true` after sending

3. **Scheduling Enabled**
   - Added `@EnableScheduling` to `BackendApplication`
   - Spring will automatically run scheduled tasks

4. **Configuration**
   - Added reminder configuration in `application.properties`
   - Configurable reminder check interval
   - Configurable hours before due date to send reminder

---

## ⚙️ Configuration

### Email Setup (Gmail Example)

1. **Enable App Password in Gmail:**
   - Go to your Google Account settings
   - Enable 2-Step Verification
   - Generate an App Password for "Mail"
   - Copy the generated password

2. **Update `application.properties`:**
   ```properties
   spring.mail.username=your-email@gmail.com
   spring.mail.password=your-app-password-here
   ```

### Reminder Configuration

In `application.properties`:
```properties
# Reminder Configuration
reminder.check.interval=900000    # Check every 15 minutes (in milliseconds)
reminder.hours.before=24          # Send reminder 24 hours before due date
```

**Configuration Options:**
- `reminder.check.interval`: How often to check for reminders (in milliseconds)
  - Default: 900000 (15 minutes)
  - Example: 3600000 = 1 hour
- `reminder.hours.before`: How many hours before due date to send reminder
  - Default: 24 hours
  - Example: 48 = 2 days before

---

## 🔄 How It Works

1. **Scheduler runs every 15 minutes** (configurable)
2. **Finds tasks** where:
   - Due date is between now and 24 hours from now
   - `reminderSent = false`
   - Task is not completed
3. **Sends email** to the task owner
4. **Marks task** as `reminderSent = true` to prevent duplicate reminders

---

## 📧 Email Format

The reminder email includes:
- Greeting with user's name
- Task title
- Task description (if available)
- Due date
- Urgency level
- Friendly reminder message

**Example Email:**
```
Hello John Doe,

This is a reminder about your upcoming task:

Task: Complete project documentation
Description: Write comprehensive docs for DayMate
Due Date: 2025-11-15T10:00:00
Urgency: HIGH

Please make sure to complete this task on time!

Best regards,
DayMate Team
```

---

## 🧪 Testing the Reminder System

### Manual Testing:

1. **Create a task with due date in the future:**
   ```json
   POST /api/tasks
   {
     "title": "Test Reminder Task",
     "description": "This task will trigger a reminder",
     "dueDate": "2025-11-13T10:00:00",  // Set to tomorrow or within 24 hours
     "urgency": "HIGH"
   }
   ```

2. **Wait for scheduler to run** (or manually trigger if needed)

3. **Check console logs** for:
   - "Processed X task reminder(s) at ..."
   - "Reminder email sent successfully for task X to ..."

4. **Check email inbox** for the reminder

### Testing with Different Time Windows:

To test immediately, you can:
1. Create a task with due date very soon (e.g., 1 hour from now)
2. Temporarily change `reminder.check.interval` to a smaller value (e.g., 60000 = 1 minute)
3. Restart the application
4. Wait for the scheduler to run

---

## 🐛 Troubleshooting

### Email Not Sending

1. **Check email configuration:**
   - Verify `spring.mail.username` and `spring.mail.password` are set
   - For Gmail, use App Password (not regular password)
   - Check console for "Email not configured" message

2. **Check console logs:**
   - Look for error messages about email sending
   - Verify scheduler is running (check for "Processed X task reminder(s)" messages)

3. **Verify task conditions:**
   - Task must have a due date
   - Task must not be completed
   - Task due date must be within the reminder window
   - Task must not have `reminderSent = true`

### Scheduler Not Running

1. **Verify `@EnableScheduling` is present** in `BackendApplication`
2. **Check application logs** for scheduler execution
3. **Verify tasks exist** with appropriate due dates

### Duplicate Reminders

- The system automatically prevents duplicates by setting `reminderSent = true`
- If you want to test again, manually reset the flag in the database

---

## 📝 Notes

- **Email is optional**: The system will continue to work even if email is not configured
- **Scheduler runs in background**: No manual intervention needed
- **One reminder per task**: Each task receives only one reminder email
- **Only incomplete tasks**: Completed tasks won't receive reminders
- **User must have email**: Tasks without a valid user email won't send reminders

---

## 🔐 Security Notes

- **Never commit email passwords** to version control
- Use environment variables or secure configuration management in production
- Consider using a dedicated email service (SendGrid, AWS SES, etc.) for production

---

## 🚀 Next Steps

The reminder system is now fully functional! You can:
1. Configure your email settings
2. Create tasks with due dates
3. Let the scheduler handle reminders automatically

For production deployment, consider:
- Using a proper logging framework instead of System.out.println
- Adding email templates (HTML emails)
- Implementing retry logic for failed emails
- Adding email delivery status tracking

