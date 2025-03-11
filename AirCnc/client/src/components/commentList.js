import React, { useEffect, useState } from "react";
import { getAllCommentByAddress, deleteComment, postComment } from "../services/comment";

export default function CommentList(props) {

    const userId = localStorage.getItem("id");
    const role = localStorage.getItem("role");
    const token = localStorage.getItem("token");
    const [comments, setComments] = useState();
    const [newComment, setNewComment] = useState();

    useEffect(() => {
        getAllCommentByAddress(props.address.id, (err, res, status) => {
            if (status === 200) {
                setComments(res);
            } else {
                console.log(status, err);
            }
        });
    }, [])

    const getDateFormat = (dateString) => {
        const date = new Date(dateString);

        const formattedDate = date.toLocaleDateString("en-US", {
            year: "numeric",
            month: "2-digit",
            day: "2-digit",
        });

        const formattedTime = date.toLocaleTimeString("en-US", {
            hour: "2-digit",
            minute: "2-digit",
        });

        const formattedDateTime = `${formattedDate} ${formattedTime}`;

        return formattedDateTime
    }

    const delComment = (comment) => {
        deleteComment(token, comment.id, (err, res, status) => {
            if (status === 200) {
                getAllCommentByAddress(props.address.id, (err, res, status) => {
                    if (status === 200) {
                        setComments(res);
                    } else {
                        console.log(status, err);
                    }
                });
            } else {
                console.log(status, err);
            }
        });
    }


    const handleSubmit = (event) => {
        event.preventDefault();

        console.log(newComment);
        postComment(token, props.address.id, newComment, (err, res, status) => {
            if (status === 201) {
                getAllCommentByAddress(props.address.id, (err, res, status) => {
                    if (status === 200) {
                        setNewComment("");
                        setComments(res);
                    } else {
                        console.log(status, err);
                    }
                });
            } else {
                console.log(status, err);
            }
        });

    };

    if (comments) {
        return (

            <ul className="max-w-md divide-y divide-gray-200 dark:divide-gray-700">

                {
                    comments.map((comment) => (
                        <li key={comment.id} className="pb-3 sm:pb-4">
                            <div className="flex flex-col space-x-4">

                                <div className="flex flex-row flex-between m-3">
                                    <p className="text-sm font-medium text-gray-900 truncate dark:text-white">
                                        {comment.username}  <span className="text-sm text-gray-500 truncate dark:text-gray-400">{getDateFormat(comment.createDate)}</span>
                                    </p>
                                </div>
                                <div className="inline-flex text-base font-semibold text-gray-900 dark:text-white">
                                    {comment.content}
                                </div>
                            </div>
                            {userId == comment.userId || role == "ROLE_ADMIN" ?
                                <p className="flex justify-end text-red-400 hover:text-red-700 hover:cursor-pointer"
                                    onClick={() => { delComment(comment) }}>Delete</p>
                                : null}

                        </li>
                    ))
                }

                {props.isAuthenticated ? <form onSubmit={handleSubmit}>
                    <div className="w-full mb-4 border border-gray-200 rounded-lg bg-gray-50 dark:bg-gray-700 dark:border-gray-600">
                        <div className="px-4 py-2 bg-white rounded-t-lg dark:bg-gray-800">
                            <label htmlFor="comment" className="sr-only">Your comment</label>
                            <textarea
                                value={newComment}
                                onChange={(e) => setNewComment(e.target.value)}
                                id="comment" rows="4" className="w-full px-0 text-sm text-gray-900 bg-white border-0 dark:bg-gray-800 focus:ring-0 dark:text-white dark:placeholder-gray-400" placeholder="Write a comment..." required></textarea>
                        </div>
                        <div className="flex items-center justify-between px-3 py-2 border-t dark:border-gray-600">
                            <button
                                type="submit" className="inline-flex items-center py-2.5 px-4 text-xs font-medium text-center text-white bg-blue-700 rounded-lg focus:ring-4 focus:ring-blue-200 dark:focus:ring-blue-900 hover:bg-blue-800">
                                Post comment
                            </button>
                        </div>
                    </div>
                </form> : null}

            </ul >
        );
    }
}
